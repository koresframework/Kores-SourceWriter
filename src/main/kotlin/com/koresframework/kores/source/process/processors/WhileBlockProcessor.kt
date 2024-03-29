/*
 *      Kores-SourceWriter - Translates Kores Structure to Java Source <https://github.com/JonathanxD/Kores-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2021 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.koresframework.kores.source.process.processors

import com.github.jonathanxd.iutils.data.TypedData
import com.koresframework.kores.base.BodyHolder
import com.koresframework.kores.base.IfExpressionHolder
import com.koresframework.kores.base.WhileStatement
import com.koresframework.kores.processor.ProcessorManager
import com.koresframework.kores.processor.processAs
import com.koresframework.kores.source.process.AppendingProcessor
import com.koresframework.kores.source.process.VARIABLE_INDEXER
import com.koresframework.kores.source.process.requireIndexer
import com.koresframework.kores.source.process.tempFrame

object WhileBlockProcessor :
    com.koresframework.kores.source.process.AppendingProcessor<WhileStatement> {

    override fun process(
        part: WhileStatement,
        data: TypedData,
        processorManager: ProcessorManager<*>,
        appender: com.koresframework.kores.source.process.JavaSourceAppender
    ) {

        if (part.type == WhileStatement.Type.DO_WHILE) {
            appender += "do"
            appender += " "
        }

        fun addWhileStm() {
            appender += "while"
            appender += " "

            val expressions = part.expressions

            if (expressions.isEmpty()) {
                appender += "("
                appender += "true"
                appender += ")"
            } else {
                processorManager.processAs<IfExpressionHolder>(part, data)
            }

        }

        if (part.type == WhileStatement.Type.WHILE) {
            addWhileStm()
            appender += " "
        }

        VARIABLE_INDEXER.requireIndexer(data).tempFrame {
            processorManager.processAs<BodyHolder>(part, data)
        }

        if (part.type == WhileStatement.Type.WHILE) {
            appender += "\n"
        } else if (part.type == WhileStatement.Type.DO_WHILE) {
            appender += " "
            addWhileStm()
            appender += ";"
            appender += "\n"
        }
    }

}