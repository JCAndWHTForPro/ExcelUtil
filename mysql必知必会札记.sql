1、limit 1,2 -> 这个表示选取第一行为起始点，步进两个！mysql中的第一行不是1，是0

2、and的优先级比or优先级高

3、排除重复列的方式是在列名字前面使用distinct，e.g:select distinct col from table

4、通配符：%->任何字符出现任何次数，匹配不了值为null的列；_->匹配单个字符。通配符尽量不要在搜索模式的开始地方使用，这种情况是最慢的搜索

5、mysql中搜索是区分大小写的

6、mysql中的正则表达式不区分大小写，如果要区分的话，要这样：REGEXP BINARY 'JetPack .000'

7、正则表达式中的相关字符意义：.代表任意一个字符；|表示或操作，表示匹配其中之一的一个字符；[]匹配特定字符；[1-9]表示匹配1到9这几个字符，类似于[123456789];\\表示转义字符，例如要匹配保留字符 . 的话，就要使用\\.进行转义；

8、正则表达式中的重复元字符：
*0个或多个匹配
+1个或多个匹配
？0个或1个匹配
{n}指定数目的匹配
{n,}不少于指定数目的匹配
{n,m}匹配数目的范围（m不超过255）

9、定位元字符
^文本的开始
$文本的结尾
[[:<:]]词的开始
[[:>:]]词的结尾

10、LTrim(col)：去除列值左边的空格
	RTrim(col)：去除列值右边的空格
	Concat(col1,col2,col3,.....)连接多个列的值，或者是字符串的值

11、COUNT()这个函数在传入的参数是*的时候，将不会忽略null的行，统计总共有多少行；如果传入的参数是一个具体的列名的时候，是会忽略null值的列进行统计的。

12、测试一下MAX()与MIN()函数的参数是varchar的情况？

13、mysql5以后加入了DISTINCT参数，可用于COUNT、AVG、SUM、MAX、MIN这些函数，作为第一个参数。如果不传入的话将默认为ALL。注意：使用COUNT的时候，DISTINCT参数存在的时候，必须要指定列名，不能使用*这种进行查询

14、在select语句中出现的非聚集函数的列名，都要在group by语句后面出现

15、select子句使用的顺序：
SELECT
FROM 
WHERE 
GROUP BY 
HAVING 
ORDER BY 
LIMIT

16、笛卡尔积：由没有连接条件的表关系返回的结果为笛卡尔积。检索出的行的数目将是第一个表中的行数乘以第二个表中的行数

17、连接分为三种，内连接（也叫做等值连接）、自连接、自然连接、外部连接
