// Copyright 2020 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.engine.language.pure.grammar.test.roundtrip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.legend.engine.language.pure.grammar.from.domain.DomainParser;
import org.finos.legend.engine.language.pure.grammar.to.DEPRECATED_PureGrammarComposerCore;
import org.finos.legend.engine.protocol.pure.m3.function.LambdaFunction;
import org.finos.legend.engine.shared.core.ObjectMapperFactory;
import org.finos.legend.engine.shared.core.api.grammar.RenderStyle;
import org.junit.Assert;
import org.junit.Test;

public class TestLambdaRoundtrip
{
    private static final ObjectMapper objectMapper = ObjectMapperFactory.getNewStandardObjectMapperWithPureProtocolExtensionSupports();

    @Test
    public void testLambdaWithINFIXoperations()
    {
        testLambda("|eq($this.var2 / $this.var1, $this.var4 / $this.var3)");
        testLambda("|eq($this.var2 / $this.var1, 3)");
        testLambda("|3->eq($this.var2 / $this.var1)");
        testLambda("|3->eq(3) && 3->eq($this.var2 / $this.var1)");
        testLambda("|isNotEmpty($this.var1 / 3)");
    }

    @Test
    public void testLambdaWithBodyWithNonStringTokenOnly()
    {
        testLambda("|a::X");
    }

    @Test
    public void testLambdaWithParameter()
    {
        testLambda("s: String[1]|'ok'");
    }

    @Test
    public void testLambdaWithTwoParameters()
    {
        testLambda("{a, b|'ok'}");
    }

    @Test
    public void testLambdaWithBodyWithString()
    {
        testLambda("|'ok'");
    }

    @Test
    public void testLambdaWithBodyWithStringMany()
    {
        testLambda("|['ok', 'ok2']");
    }

    @Test
    public void testLambdaWithBodyWithInteger()
    {
        testLambda("|1");
    }

    @Test
    public void testLambdaWithBodyWithIntegerMany()
    {
        testLambda("|[1, 2]");
    }

    @Test
    public void testLambdaWithBodyWithBoolean()
    {
        testLambda("|true");
    }

    @Test
    public void testLambdaWithBodyWithBooleanMany()
    {
        testLambda("|[true, false]");
    }

    @Test
    public void testLambdaWithBodyWithFloat()
    {
        testLambda("|1.23");
    }

    @Test
    public void testLambdaWithBodyWithFloatMany()
    {
        testLambda("|[1.23, 1.54]");
    }

    @Test
    public void testLambdaWithBodyWithStrictDate()
    {
        testLambda("|%2010-10-20");
    }

    @Test
    public void testLambdaWithBodyWithStrictDateMany()
    {
        testLambda("|[%2010-10-20, %2010-10-22]");
    }

    @Test
    public void testLambdaWithBodyWithDateTime()
    {
        testLambda("|%2010-10-20T20:20:20.000");
    }

    @Test
    public void testLambdaWithBodyWithDateTimeMany()
    {
        testLambda("|[%2010-10-20T20:20:20.000, %2010-10-22T00:00:00.000]");
    }

    @Test
    public void testLambdaWithBodyWithStrictTime()
    {
        testLambda("|%22:22:22.1111");
    }

    @Test
    public void testLambdaWithBodyWithStrictTimeMany()
    {
        testLambda("|[%22:22:22.1, %09:20:21, %8:08:08]");
    }

    @Test
    public void testLambdaWithGetAll()
    {
        testLambda("|Person.all()->filter(p|true)");
    }

    @Test
    public void testLambdaWithFilterWithTypeInParam()
    {
        testLambda("|Person.all()->filter(p: Person[1]|true)");
    }

    @Test
    public void testLambdaWithFilterWithEqualExpression()
    {
        testLambda("|Person.all()->filter(p|$p.name == 'ok')");
    }

    @Test
    public void testLambdaWithFilterWithGreaterThanExpression()
    {
        testLambda("|Person.all()->filter(p|$p.age > 10)");
    }

    @Test
    public void testLambdaWithFilterWithGreaterThanEqualsExpression()
    {
        testLambda("|Person.all()->filter(p|$p.age >= 10)");
    }

    @Test
    public void testLambdaWithFilterWithLessThanExpression()
    {
        testLambda("|Person.all()->filter(p|$p.age < 10)");
    }

    @Test
    public void testLambdaWithFilterWithLessThanEqualsExpression()
    {
        testLambda("|Person.all()->filter(p|$p.age <= 10)");
    }

    @Test
    public void testLambdaWithFilterWithFunctionExpression()
    {
        testLambda("|Person.all()->filter(p|$p.name->startsWith('ok'))");
    }

    @Test
    public void testLambdaWithMapWithPlusFunctionExpression()
    {
        testLambda("|Person.all()->map(p|$p.name + 'testLambda')");
    }

    @Test
    public void testLambdaWithMultiPlus()
    {
        testLambda("|'ok' + 'b' + 'c'");
    }

    @Test
    public void testLambdaWithMultiTimes()
    {
        testLambda("|'ok' * 'b' * 'c'");
    }

    @Test
    public void testLambdaWithNot()
    {
        testLambda("|!(true != false)");
        testLambda("|true != false");
        testLambda("|!(true == false)", "|true != false");
        testLambda("|not(true == false)", "|true != false");
        testLambda("|not(true != false)", "|!(true != false)");
    }




    @Test
    public void testLambdaGroupingLeftSide()
    {
        testLambda("|(1+2)->process();","|process(1 + 2)");
    }

    @Test
    public void testArithmetic()
    {
        testLambda("|2 * 4 + 2", "|(2 * 4) + 2");
        testLambda("|2 + 2 * 4", "|2 + (2 * 4)");
        testLambda("|2 + 2 / 4", "|2 + (2 / 4)");
        testLambda("|2 / 4 - 4", "|(2 / 4) - 4");
    }

    @Test
    public void testBoolean()
    {
        testLambda("|true && false || true", "|(true && false) || true");
        testLambda("|true || true && false", "|true || (true && false)");
    }

    @Test
    public void testEqualAndNotEqual()
    {
        testLambda("|true == false && true == false", "|(true == false) && (true == false)");
        testLambda("|true == false && true == false || false == false", "|((true == false) && (true == false)) || (false == false)");
        testLambda("|true == false || true == false && false == false", "|(true == false) || ((true == false) && (false == false))");
        testLambda("|true == !false || !(true == false) && false == false", "|(true == !false) || ((true != false) && (false == false))");
        testLambda("|not(true != false && true == !false)", "|!((true != false) && (true == !false))");
        testLambda("|not(true != false && true == !false || !(true && false && true == !false))", "|!(((true != false) && (true == !false)) || !((true && false) && (true == !false)))");
        testLambda("|true && false == $x->isNumber()","|true && (false == $x->isNumber())");
        testLambda("|$x->isNumber() && false == $x->isNumber()", "|$x->isNumber() && (false == $x->isNumber())");
        testLambda("|$x->isNumber() && false == true", "|$x->isNumber() && (false == true)");
        testLambda("|$this.time == 'www'");
        testLambda("|$this.time == %10:12:20");
    }

    @Test
    public void testLambdaArrowProcessingOrder()
    {
        // The following expressions may not 'compile' but the precedence needs to be respected.
        testLambda("|!(true == false)->f()", "|!f(true == false)");
        testLambda("|(!(true == false))->f()", "|f(true != false)");
        testLambda("|(true != false)->f()", "|f(true != false)");
        testLambda("|(!(true != !false))->f()");
        testLambda("|(!1 > 2)->f()", "|f(!1 > 2)");
        testLambda("|(!(1 > 2))->f()");
        testLambda("|(!(1 < 2))->f()");
        testLambda("|(!true && false)->f()", "|f(!true && false)");
        testLambda("|!true && false->f()", "|!true && f(false)");
        testLambda("|-1->f()", "|-f(1)");
        testLambda("|(-1)->f()");
        testLambda("|(2 - 1)->f()");
        testLambda("|-(2 - 1)->f()");
        testLambda("|(-(2 - 1))->f()");
        testLambda("|(-(2 * -1))->f()", "|(-(2 * -1))->f()");
        testLambda("|(!true)->f()");
        testLambda("|!true->f()", "|!f(true)");
        testLambda("|!$c.salesPerson.lastName->in($names)");
        testLambda("|(!$c.salesPerson.lastName)->in($names)");
        testLambda("|(!$x->isNumber())->f()");
        testLambda("|!$x->isNumber()->f()");
    }

    @Test
    public void testGroup()
    {
        testLambda("|5 * (1 + 2)");
    }

    @Test
    public void testLambdaWithAnd()
    {
        testLambda("|(1 < 2) && (2 < 3)");
    }

    @Test
    public void testLambdaWithMultiMinus()
    {
        testLambda("|'ok' - 'b' - 'c'");
    }

    @Test
    public void testLambdaWithMapWithMinusFunctionExpression()
    {
        testLambda("|Person.all().age->map(p|$p - 10)");
    }

    @Test
    public void testLambdaWithMapWithTimesFunctionExpression()
    {
        testLambda("|Person.all().age->map(p|$p * 10)");
    }

    @Test
    public void testLambdaWithMapWithDivFunctionExpression()
    {
        testLambda("|Person.all().age->map(p|$p / 10)");
    }

    @Test
    public void testLambdaWithAllVersions()
    {
        testLambda("|Person.allVersions()");
    }

    @Test
    public void testLambdaWithLambdaWithEnumWithPackage()
    {
        testLambda("|a::b::MyEnum.ok");
    }

    @Test
    public void testLambdaWithEnum()
    {
        testLambda("|MyEnum.ok");
        testLambda("|MyEnum.'30_360'");
    }

    @Test
    public void testLambdaWithProjectWithCol()
    {
        testLambda("|Person.all()->filter(f|$f.name->startsWith('ok'))->project([col(p|$p.name, 'ok')])");
    }

    @Test
    public void testLambdaWithAndUsedAsACollectionFunction()
    {
        testLambda("|[true, false, false]->and()");
    }

    @Test
    public void testLambdaWithAndFromVariable()
    {
        testLambda("|$i->and()");
    }

    @Test
    public void testLambdaWithPlusFromVariable()
    {
        testLambda("|$i->plus()");
    }

    @Test
    public void testGraphFetch()
    {
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId,lei,eti}}#)");
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId,'ok':lei,eti}}#)");
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId{other,and{more}},lei,eti}}#)");
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId(true),lei,eti}}#)");
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId(true),lei($var),eti('yo'),opi(1.23)}}#)");
        testLambda("|MifidCDTModel.all()->graphFetch(#{MifidCDTModel{cPrimeId(true),lei->subType(@SomeClass),eti('yo'),opi(1.23)->subType(@SomeOtherClass){and{more}}}}#)");
    }

    @Test
    public void testDslNavigationPath()
    {
        testLambda("|$this.employees.lastName->sortBy(#/model::Person/lastName#)->joinStrings('')");
        testLambda("|#/Person/nameWithPrefixAndSuffix('a', 'b')#");
        testLambda("|#/Person/nameWithPrefixAndSuffix('a', ['a', 'b'])#");
        testLambda("|#/Person/nameWithPrefixAndSuffix([], ['a', 'b'])#");
        testLambda("|#/Person/nameWithPrefixAndSuffix('a', [1, 2])#");
    }

    @Test
    public void testAppliedFunctionWithParametersUsingInfixOperations()
    {
        testLambda("|'data1' + toString(someTransformation::fn('data1') + 1)");
    }

    @Test
    public void testFunctionNameWithRichIdentifier()
    {
        testLambda("|someTransformation::fn('data1') + 1");
        testLambda("|someTransformation::fn::'special function 1'('data1') + 1");
    }

    @Test
    public void testLambdaWithCast()
    {
        testLambda("src: OldClass[1]|$src->cast(@newClass)");
    }

    @Test
    public void testLambdaWithQuotedPropertyAccess()
    {
        testLambda("|$this.'1@3'->isEmpty()");
    }

    @Test
    public void testLambdaWithBiTemporalClassInPropertyExpression()
    {
        testLambda("|Person.all()->project([col(a|$a.firm(%latest, %latest), 'a')])");
    }

    @Test
    public void testRenderingFunctionExpressionWithSinglePrimitiveArgument()
    {
        testLambda("|sort('car')");
        testLambda("|reverse(true)");
        testLambda("|reverse(false)");
        testLambda("|abs(4)");
        testLambda("|abs(4.0)");
        testLambda("|round(4.04)");
        testLambda("|someDateFn(%9999-12-30)");
        testLambda("|someDateFn(%9999-12-30T19:00:00.0000)");

        testLambda("|'car'->sort()", "|sort('car')");
        testLambda("|true->reverse()", "|reverse(true)");
        testLambda("|false->reverse()", "|reverse(false)");
        testLambda("|4->abs()", "|abs(4)");
        testLambda("|4.0->abs()", "|abs(4.0)");
        testLambda("|4.04->round()", "|round(4.04)");
        testLambda("|%9999-12-30->someDateFn()", "|someDateFn(%9999-12-30)");
        testLambda("|%9999-12-30T19:00:00.0000->someDateFn()", "|someDateFn(%9999-12-30T19:00:00.0000)");
    }

    @Test
    public void testLambdaEndingOnComment()
    {
        testLambda("|1 + 1// comment here", "|1 + 1");
    }

    static void testLambda(String text)
    {
        testLambda(text, text, RenderStyle.STANDARD);
    }

    static void testLambda(String text, String formattedText)
    {
        testLambda(text, formattedText, RenderStyle.STANDARD);
    }

    static void testLambdaPretty(String text)
    {
        testLambda(text, text, RenderStyle.PRETTY);
    }

    static void testLambdaPretty(String text, String formattedText)
    {
        testLambda(text, formattedText, RenderStyle.PRETTY);
    }

    static void testLambda(String text, String formattedText, RenderStyle renderStyle)
    {
        LambdaFunction postJSON_lambda;
        try
        {
            LambdaFunction lambda = new DomainParser().parseLambda(text, "", 0, 0, true);
            String json = objectMapper.writeValueAsString(lambda);
            postJSON_lambda = objectMapper.readValue(json, LambdaFunction.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(formattedText, postJSON_lambda.accept(DEPRECATED_PureGrammarComposerCore.Builder.newInstance().withRenderStyle(renderStyle).build()));
    }
}
