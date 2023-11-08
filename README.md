# mbx2svg

This is a converter for [MindBoard](https://mindboard.github.io/mindboard-pro-user-guide/) / [MindBoard 2019](https://mindboard.github.io/mindboard-2019-user-guide/) mbx data to SVG.


## Build

To build, java 17 is required. 

To build executable jar:

```plaintext
./gradlew bootJar
```

The jar file is in build/libs/mbx2svg-[version].jar.


## Usage

```plaintext
java -jar mbx2svg-[version].jar foo.mbx -o foo.svg
```


## Demo

You can convert Demo/example.mbx data to SVG.

This SVG:

[<img src="https://raw.githubusercontent.com/mindboard/mbx2svg/main/Demo/example.jpg" width="320px">](https://raw.githubusercontent.com/mindboard/mbx2svg/main/Demo/example.svg)

