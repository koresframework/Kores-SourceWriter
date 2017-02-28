/*
 *      CodeAPI-SourceWriter - Framework to generate Java code and Bytecode code. <https://github.com/JonathanxD/CodeAPI-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.codeapi.source.gen.generator

import com.github.jonathanxd.codeapi.CodeSource
import com.github.jonathanxd.codeapi.base.BodyHolder
import com.github.jonathanxd.codeapi.base.IfExpressionHolder
import com.github.jonathanxd.codeapi.base.IfStatement
import com.github.jonathanxd.codeapi.common.Data
import com.github.jonathanxd.codeapi.gen.value.CodeSourceData
import com.github.jonathanxd.codeapi.gen.value.Parent
import com.github.jonathanxd.codeapi.gen.value.Value
import com.github.jonathanxd.codeapi.gen.value.ValueGenerator
import com.github.jonathanxd.codeapi.source.gen.PlainSourceGenerator
import com.github.jonathanxd.codeapi.source.gen.value.CodePartValue
import com.github.jonathanxd.codeapi.source.gen.value.PlainValue
import com.github.jonathanxd.codeapi.source.gen.value.TargetValue
import java.util.*

object IfStatementSourceGenerator : ValueGenerator<IfStatement, String, PlainSourceGenerator> {

    override fun gen(inp: IfStatement, c: PlainSourceGenerator, parents: Parent<ValueGenerator<*, String, PlainSourceGenerator>>, codeSourceData: CodeSourceData, data: Data): List<Value<*, String, PlainSourceGenerator>> {

        val values = ArrayList<Value<*, String, PlainSourceGenerator>>()

        val isElvis = !Util.isBody(parents)

        if(!isElvis)
            values.add(PlainValue.create("if "))

        values.add(TargetValue.create(IfExpressionHolder::class.java, inp, parents))

        val elseStatement = inp.elseStatement

        if (elseStatement.isEmpty) {
            if(!isElvis) {
                // Clean body
                values.add(TargetValue.create(BodyHolder::class.java, inp, parents))
            } else {
                val body = inp.body

                if(body.size != 1)
                    throw IllegalStateException("Elvis if expression must have only one element in the body!");

                values.add(CodePartValue.create(body.single(), parents))
            }
        } else {

            if(!isElvis) {
                values.add(TargetValue.create(BodyHolder::class.java, inp, parents))
                values.add(PlainValue.create(" else "))
                values.add(TargetValue.create(CodeSource::class.java, elseStatement, parents))
                values.add(PlainValue.create("\n"))
            } else {
                val body = inp.body

                if(body.size != 1)
                    throw IllegalStateException("Elvis if expression must have only one element in the body!")

                if(elseStatement.size != 1)
                    throw IllegalStateException("Elvis else expression must have only one element in the body!")

                values.add(PlainValue.create(" ? "))

                values.add(CodePartValue.create(body.single(), parents))

                values.add(PlainValue.create(" : "))

                values.add(CodePartValue.create(elseStatement.single(), parents))

            }

        }

        return values
    }

}
