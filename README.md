#             Simple-C

欢迎感兴趣的小伙伴联系我, 博客地址 : http://www.cnblogs.com/nzhl/

## 关于实现方式

词法分析部分使用的是手工编码的方式, 语法分析部分也是手撸, 用的是递归下降… 生成抽象语法语法树之后, 有一个简单的类型检查, 之后直接生成了较为简单的栈式机器指令... 指令是自己编写的, 所以为了执行这些指令临时手撸了一个简单的解释器, 通过解释器将指令解释并使用java执行... 由于解释器书写仓促导致十分简陋以及各方面原因, 该语言目前来讲还不算是一门特别完整的语言… 之后还会继续改进...

## 关于演示

源代码 :

```c
int main1(){
    print "这是函数1.....";
    int x = 1;
    while(x < 10){
        print x;
        x = x + 1;
    }
}

int main2(){
    print " ";
    print " ";
    print " ";
    print " ";
    print "这是函数2.....";
    print "上面和下面的变量属于同一个命名空间, 所以再重复定义x会报错...";
    int y = 1;
    int z = 2;
    if(y > z){
        print "不可能执行这里!";
    }else {
        print "没错就是这里";
        if(y == 1){
            print "支持if嵌套";
        }
    }
}
```

生成的栈式指令 :

```
push 这是函数1.....
print
push 1
.int x
store x
while
load x
push 10
l
then
load x
print
load x
push 1
add
store x
end
push  
print
push  
print
push  
print
push  
print
push 这是函数2.....
print
push 上面和下面的变量属于同一个命名空间, 所以再重复定义x会报错...
print
push 1
.int y
store y
push 2
.int z
store z
load y
load z
g
if
push 不可能执行这里!
print
otherwise
push 没错就是这里
print
load y
push 1
equal
if
push 支持if嵌套
print
otherwise
end
end

```

最终输出结果

```bash
----------------->   Check your syntax ...
----------------->   Finish ...



----------------->   Generate the abstract syntax tree ...
----------------->   Finish ...



----------------->   Type check ...
----------------->   Have generated the aim code successfully !!!


-----------------   Running result   ----------------- 


这是函数1.....
1
2
3
4
5
6
7
8
9
 
 
 
 
这是函数2.....
上面和下面的变量属于同一个命名空间, 所以再重复定义x会报错...
没错就是这里
支持if嵌套
```

## 关于语言风格

目前实现的是及其简化版本的C语言… 但又不完全是属于C语言的子集, 加入了对于字符串以及布尔类型的支持...

## 关于上下文语法

```
prog -> func funcs
      | 

func -> type id () block

block -> { stmts }

stmts -> stmt stmts
 　 　 | 

stmt -> type id;
      | type id = judge;
      | id = judge;
      | if(judge) block
      | if(judge) block else block
      | while(judge) block
      | print judge;

type -> int
      | bool
      | string

judge -> judge \| join
       | join

join  -> join & equality
       | equality

equality -> equality == rel
          | equality != rel
          | rel

rel -> expr < expr 
     | expr <= expr
     | expr >= expr
     | expr > expr
     | expr

expr -> expr + term
      | expr - term
      | term

term -> term * unary
      | term / unary
      | unary

unary -> !unary
       | -unary
       | factor

factor -> num
        | boolean
        | str
        | id
        | (judge)
boolean -> True | False

id : [a-zA-Z_][0-9a-zA-Z_]*

num : -?[0-9]+

str : "[\"|.]*"

```

## 关于缺陷

1. 目前只支持线性函数执行, 不支持函数间相互调用... 也就是函数会根据定义顺序一个一个的执行...
2. 目前所有的变量都共用一个命名空间, 也就是说不管是在if, while甚至另外一个函数中, 都不能重复命名变量, 否则报错...
3. 只支持if的嵌套使用, 不支持while...
4. print只能打印三种基本类型以及 id, 打印id会将id所对应的值打印出来...
5. 对于字符串str类型, 目前来说除了可以打印和比较大小, 其他包括拼接, 截取等操作都不行... 同时不支持'\n', '\t'等等带逃脱符的特殊字符, 默认print会换行, 所以换行最好的办法打印空格...
6. if, else, while, 字符串以及函数内部均不能为空, 否则视为语法错误...

## 关于如何运行...

1. 打开目录下virtualMachine/Config.java...设置源文件代码所在目录和输入文件代码所在目录, 记得要用绝对路径...
2. 终端进入源代码所在目录的顶层目录, 就是有`run.sh`的目录...然后执行终端指令`sudo bash run.sh`即可看到结果...
3. 有任何bug欢迎给我提request或者直接联系我…博客地址在上面...

