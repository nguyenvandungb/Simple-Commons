package com.ts.commons.extensions

import java.io.BufferedWriter

fun BufferedWriter.writeLn(line: String) {
    write(line)
    newLine()
}
