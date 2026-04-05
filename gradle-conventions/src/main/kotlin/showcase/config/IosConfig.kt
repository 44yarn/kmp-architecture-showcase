package showcase.config

object IosConfig {
    const val BUNDLE_ID_PREFIX = "io.github.mitsuharu.showcase"
    const val IOS_DEPLOYMENT_TARGET = "17.0"

    fun getBundleId(moduleName: String): String =
        "$BUNDLE_ID_PREFIX.${moduleName.replace(":", ".").removePrefix(".")}"
}
