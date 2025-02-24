package org.jetbrains.research.testspark.core.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandLineRunner {
    companion object {
        protected val log = KotlinLogging.logger {}

        /**
         * Executes a command line process and returns the output as a string.
         *
         * @param cmd The command line arguments as an ArrayList of strings.
         * @return The output of the command line process as a string.
         */
        fun run(cmd: ArrayList<String>): String {
            var errorMessage = ""

            /**
             * Since Windows does not provide bash, use cmd or similar default command line interpreter
             */
            val process = if (DataFilesUtil.isWindows()) {
                ProcessBuilder()
                    .command("cmd", "/c", cmd.joinToString(" "))
                    .redirectErrorStream(true)
                    .start()
            } else {
                log.info { "Running command: ${cmd.joinToString(" ")}" }
                ProcessBuilder()
                    .command("bash", "-c", cmd.joinToString(" "))
                    .redirectErrorStream(true)
                    .start()
            }

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                errorMessage += line
            }

            process.waitFor()

            return errorMessage
        }
    }
}
