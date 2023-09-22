package com.arefdev.base.utils

import com.jaredrummler.ktsh.Shell

/**
 * Updated on 21/08/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ShellUtils {

    @JvmStatic
    fun canRunRootCommands(): Boolean {
        return Shell.SU.run("id").let {
            val currUid = it.stdout()
            if (currUid.contains("uid=0")) {
                log("ROOT", "Root access granted")
            } else {
                log("ROOT", "Root access rejected: $currUid")
            }
            it.isSuccess
        }
    }

    @JvmStatic
    fun changeTime(c: CalendarTool = CalendarTool.now()): Boolean {
        // Format: MMDDhhmmYYYY.ss
        val hh = c.hour.toString().padStart(2, '0')
        val mm = c.minute.toString().padStart(2, '0')
        val ss = c.second.toString().padStart(2, '0')
        val MM = c.month.toString().padStart(2, '0')
        val DD = c.day.toString().padStart(2, '0')
        val YYYY = c.year.toString()
        return Shell.SU.run("date $MM$DD$hh$mm$YYYY.$ss").let {
            if (it.isSuccess) {
                log("ChangeTime", "Result: ${it.stdout()}")
            } else {
                log("ChangeTime", "Can't get root access or denied by user")
            }
            it.isSuccess
        }
    }

    @JvmStatic
    fun killApp(pkgName: String): Boolean {
        return Shell.SU.run("am force-stop $pkgName").isSuccess
    }

    @JvmStatic
    fun installApk(path: String): Boolean {
        return Shell.SU.run("pm install $path").isSuccess
    }

    @JvmStatic
    fun reboot(): Boolean {
        return Shell.SU.run("reboot").isSuccess
    }

    @JvmStatic
    fun pressPowerKey(): Boolean {
        return Shell.SU.run("input keyevent KEYCODE_POWER").let {
            if (it.isSuccess) {
                log("PressPowerKey", "Result: ${it.stdout()}")
            } else {
                log("PressPowerKey", "Can't get root access or denied by user")
            }
            it.isSuccess
        }
    }

    @JvmStatic
    fun isScreenOn(): Boolean {
        return Shell.SU.run("dumpsys power | grep mWakefulness=").let {
            if (it.isSuccess) {
                log("isScreenOn", "Result: ${it.stdout()}")
            } else {
                log("isScreenOn", "Can't get root access or denied by user")
            }
            it.isSuccess
        }
    }

    @JvmStatic
    fun topActivities(): List<String> {
        val list = mutableListOf<String>()
        try {
            Shell.SH.let { shell ->
                val result = shell.run("dumpsys activity activities | grep -E 'app=ProcessRecord|intent='")
                if (result.isSuccess) {
//                    log("topActivities", result.stdout())
                    val stdOut = result.stdout
                    try {
                        for (i in 0 until stdOut.size / 2 step 2) {
                            val cmp = stdOut[i].trim().split("cmp=")[1].let { it.substring(0 until it.length - 1) }
                            val pid = stdOut[i + 1].trim().split(" ")[1].split(':')[0]
                            list.add("$pid $cmp")
                        }
//                    for (item in stdOut) {
//                        item.split("ActivityRecord").takeIf { it.isNotEmpty() }?.let {
//                            list.add(it[1].substring(1, it[1].length - 1).split(" ").slice(2..3).joinToString(" "))
//                        }
//                    }
                    } catch (_: Exception) {
                    }
                } else {
                    logE("topActivities", result.stderr())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            logE("topActivities", "Error: $e")
        }
        return list
    }
}