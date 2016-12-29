/*
 *      CodeAPI-SourceWriter - Framework to generate Java code and Bytecode code. <https://github.com/JonathanxD/CodeAPI-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.codeapi.source.gen.value

import com.github.jonathanxd.codeapi.CodeSource
import com.github.jonathanxd.codeapi.gen.Appender
import com.github.jonathanxd.codeapi.gen.value.AbstractGenerator
import com.github.jonathanxd.codeapi.gen.value.CodeSourceData
import com.github.jonathanxd.codeapi.gen.value.Value
import com.github.jonathanxd.codeapi.gen.value.ValueGenerator
import com.github.jonathanxd.codeapi.util.Parent
import com.github.jonathanxd.iutils.data.MapData

/**
 * [Value] that holds [CodeSource].
 * This value call generator of each element in provided [.source].
 *
 * @param TARGET   Result Object type.
 * @param C        Generator type.
 */
class CodeSourceValue<TARGET, C : AbstractGenerator<TARGET, C>>(private val source: CodeSource, val parents: Parent<ValueGenerator<*, TARGET, C>>) : Value<CodeSource, TARGET, C> {

    override fun apply(value: TARGET, abstractGenerator: C, appender: Appender<TARGET>, codeSourceData: CodeSourceData, data: MapData) {

        source.forEach { part ->
            val call = abstractGenerator.generateTo(part.javaClass /*as Class<? extends CodePart>*/, part, parents, codeSourceData, data)

            if (call != null && !call.isEmpty()) {
                for (genValue in call) {
                    AbstractGenerator.helpApply(genValue, part, abstractGenerator, appender, codeSourceData, data)
                }
            } else {
                throw IllegalStateException("Cannot find generator for '" + part.javaClass.canonicalName + "'")
            }
        }
    }

    override fun getValue(): CodeSource {
        return source
    }

    companion object {

        /**
         * Create the value
         *
         * @param source   Code Source
         * @param current  Parent generators (bug tracing).
         * @param TARGET   Result Object type.
         * @param C        Generator type.
         * @return [CodeSourceValue]
         */
        fun <TARGET, C : AbstractGenerator<TARGET, C>> create(source: CodeSource, current: Parent<ValueGenerator<*, TARGET, C>>): Value<CodeSource, TARGET, C> {
            return CodeSourceValue(source, current)
        }
    }

}
