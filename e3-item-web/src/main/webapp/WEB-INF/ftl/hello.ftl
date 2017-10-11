${hello}
//遍历list
<#list studentList as student>
	//取当前的序号
	${student_index}
	
<#if student_index %2==0>
//有分支的话 else得写在if内
	<#else >
</#if>

</#list>	
//获取日期 时间
${date?datetime}
//获取日期
${date?date}
//获取时间
${date?time}
//指定输出格式
${date?string("yyyy-MM-dd HH:mm:ss")}
//对null值的处理，后面加！，！后面可以跟默认值，也可以不加
${val!}
//对null值的处理方式2
<#if val??>
val中有内容
<#else>
val值为空
</#if>
//引用模板测试
<#include "hello.ftl">

