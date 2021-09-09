### 1. Magic Number

- 大小：两个字节。

- 内容：CA FE BA BE

  解读: 标准class文件



### 2. Minor Version
- 大小：两字节
- 内容：00 00

  解读：小版本号为0.

### 3. Major Version
- 大小：两字节
- 内容：00 34

  解读：版本号为52；表示为JDK1.8 编译的类

### 4. constant_pool_count
- 大小：两字节
- 内容：00 10

  解读：常量池大小为15；需要在对应值基础上-1


### 5. constant_pool []

- 格式：
一个字节表示类型；


#### 5.1 CONSTANT_Methodref_info (0A:10)
- part1：00 03

  解读：类型为 CONSTANT_class_info，位置为 #3 号常量。
- part2: 00 0D

  解读: 类型为 CONSTANT_NameAndType，指向 #13 号常量。

> 总结：共5个字节，表示常量池的 #1 号。


#### 5.2 CONSTANT_class_info (07:07)

- 内容：00 0E

  解读：类型为类全路径名称。具体内容指向 #14 号常量。

> 总结：共3字节，表示常量池的 #2 号



#### 5.3 CONSTANT_class_info (07:07)。 被 #1 号引用

- 内容：00 0F

  解读：类名称。 指向 #15 号常量。

> 总结：3字节； 表常量池 #3 号


#### 5.4 CONSTANT_utf8_info (01:01)

- part1 : 00 06 (注意只看两个字节)

  解：内容长度为6个字节。
- p2：3C 69 6E 69 74 3E

  解：对应内容为 <init>。 JVM 内部语法，表示构造函数

> 总结： 9字节；常量池 #4 号


#### 5.5 CONSTANT_Utf8_info (01:01)

- p1: 00 03

  解：内容长度为3字节。
- p2: 28 29 56

  解：内容：()V； 表示无入参，返回值为 空Void。


#### 5.6 CONSTANT_Utf8_info (01:01)

- p1: 00 04

  解：内容长度为4字节。
- p2: 43 6F 64 65

  解：内容：Code。





















