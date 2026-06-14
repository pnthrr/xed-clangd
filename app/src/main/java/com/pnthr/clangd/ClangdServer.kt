package com.pnthr.clangd

import android.app.Activity
import android.content.Context
import com.rk.exec.isTerminalInstalled
import com.rk.file.child
import com.rk.file.sandboxHomeDir
import com.rk.icons.Icon
import com.rk.lsp.LspConnectionConfig
import com.rk.lsp.ScriptedLspServer
import java.io.File

class ClangdServer(
    override val icon: Icon,
    override val supportedExtensions: List<String>,
    override val installScript: File,
) : ScriptedLspServer() {
    override val id = "clangd"
    override val languageName = "C/C++"
    override val serverName = "clangd"
    override val installId = "clangd language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) return false
        return sandboxHomeDir(context).child(".lsp/clangd/installed").exists()
    }

    override fun install(activity: Activity) = launchInstaller(activity)

    override fun uninstall(activity: Activity) = launchInstaller(activity, "--uninstall")

    override fun update(activity: Activity) = launchInstaller(activity, "--update")

    override suspend fun isUpdatable(context: Context): Boolean = false

    override fun getConnectionConfig(): LspConnectionConfig {
        return LspConnectionConfig.Process(arrayOf("/usr/bin/clangd", "--fallback-style=LLVM"))
    }
}
