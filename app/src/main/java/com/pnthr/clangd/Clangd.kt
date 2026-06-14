package com.pnthr.clangd

import android.app.Activity
import android.os.Bundle
import androidx.annotation.Keep
import com.rk.file.BuiltinFileType
import com.rk.file.child
import com.rk.lsp.LspRegistry
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.utils.getTempDir

@Keep
@Suppress("unused")
class Clangd(context: ExtensionContext) : ExtensionAPI(context) {
    private var clangdServer: ClangdServer? = null

    override fun onExtensionLoaded() {
        val installScript = getTempDir().child("clangd-lsp.sh").also {
            context.assets.open("clangd-lsp.sh").use { input ->
                it.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        val icon = BuiltinFileType.CPP.icon!!  // or BuiltinFileType.C.icon!!
        val extensions = BuiltinFileType.C.extensions + BuiltinFileType.CPP.extensions

        clangdServer = ClangdServer(
            icon = icon,
            supportedExtensions = extensions.distinct(),
            installScript = installScript,
        ).also {
            LspRegistry.registerServer(it)
        }
    }

    private fun dispose() {
        clangdServer?.let {
            LspRegistry.unregisterServer(it)
        }
    }

    override fun onInstalled() {}
    override fun onUpdated() { dispose(); onExtensionLoaded() }
    override fun onUninstalled() { dispose() }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
}
