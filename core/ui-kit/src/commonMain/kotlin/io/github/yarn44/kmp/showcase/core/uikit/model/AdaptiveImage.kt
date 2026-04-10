package io.github.yarn44.kmp.showcase.core.uikit.model

import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource

/**
 * Unifies remote image URLs and local drawable [DrawableResource]s into a
 * single consumer-facing type.
 *
 * ## Why this exists
 *
 * Similar in spirit to [AdaptiveString]: a UI field (e.g. an avatar slot
 * or a card thumbnail) may receive either a remote URL (server-provided)
 * or a local drawable resource (shipped with the app). Consumers should
 * not have to branch between the two — they just hand the image to the
 * renderer and expect it to draw.
 *
 * ## Construction
 *
 * The primary constructor is private to prevent callers from supplying
 * invalid combinations of [url] and [resource]. Use one of the public
 * secondary constructors:
 *
 * - `AdaptiveImage(url: String, type: ImageType = Icon)` -> remote URL
 * - `AdaptiveImage(resource: DrawableResource, type: ImageType = Icon)` -> local
 *
 * ## Rendering
 *
 * Rendering is intentionally left to the caller. A Composable renderer
 * can branch on [type] to apply the right sizing/scaling, and pick
 * between `AsyncImage(url)` and `painterResource(resource)` depending on
 * which field is populated. An iOS-side renderer can do the same with
 * SwiftUI `AsyncImage` and a bundled asset lookup.
 */
class AdaptiveImage private constructor(
    internal val url: String? = null,
    internal val resource: DrawableResource? = null,
    internal val type: ImageType = ImageType.Icon,
) {
    /** Remote image URL. */
    constructor(url: String, type: ImageType = ImageType.Icon) : this(
        url = url,
        resource = null,
        type = type,
    )

    /** Local drawable resource. */
    constructor(resource: DrawableResource, type: ImageType = ImageType.Icon) : this(
        url = null,
        resource = resource,
        type = type,
    )

    /** Describes how the image should be displayed by the renderer. */
    sealed class ImageType {
        /** Small, square, icon-style image (no forced aspect ratio). */
        data object Icon : ImageType()

        /** Stretched to the container's full width with a fixed aspect ratio. */
        data class FillMaxWidth(
            val contentScale: ContentScale = ContentScale.Crop,
            val aspectRatio: Float = DEFAULT_ASPECT_RATIO,
        ) : ImageType() {
            private companion object {
                const val DEFAULT_ASPECT_RATIO = 16f / 9f
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdaptiveImage) return false
        if (url != other.url) return false
        if (resource != other.resource) return false
        if (type != other.type) return false
        return true
    }

    override fun hashCode(): Int {
        var result = url?.hashCode() ?: 0
        result = 31 * result + (resource?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        return result
    }
}
