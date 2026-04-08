package showcase.primitive

import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop

plugins {
    id("co.touchlab.skie")
}

skie {
    features {
        group {
            coroutinesInterop.set(true)
            SuspendInterop.Enabled(true)
            FlowInterop.Enabled(true)
            SealedInterop.Enabled(true)
        }
    }
}
