package com.mindboardapps.app.mb.mbx2svg

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest
class CmdLineParserTests {

	/*
	@Test
	fun contextLoads() {
	}
	*/

	@Test
	fun test1(){
		val args = "foo.mbx -o foo.svg".split(" ")
		val value = CmdLineParser.toOutputOptionValue(args)
		assertThat(value).isEqualTo("foo.svg")
    }

	@Test
	fun test2(){
		val args = "foo.mbx --output foo.svg".split(" ")
		val value = CmdLineParser.toOutputOptionValue(args)
		assertThat(value).isEqualTo("foo.svg")
    }

	@Test
	fun test3(){
		val args = "--output foo.svg foo.mbx".split(" ")
		val value = CmdLineParser.toOutputOptionValue(args)
		assertThat(value).isEqualTo("foo.svg")
    }

	@Test
	fun test11(){
		val args = "foo.mbx -o foo.svg".split(" ")
		val value = CmdLineParser.toInputOptionValue(args)
		assertThat(value).isEqualTo("foo.mbx")
    }

	@Test
	fun test12(){
		val args = "foo.mbx --output foo.svg".split(" ")
		val value = CmdLineParser.toInputOptionValue(args)
		assertThat(value).isEqualTo("foo.mbx")
    }

	@Test
	fun test13(){
		val args = "--output foo.svg foo.mbx".split(" ")
		val value = CmdLineParser.toInputOptionValue(args)
		assertThat(value).isEqualTo("foo.mbx")
    }

	@Test
	fun test21(){
		val args = "foo.mbx -o foo.svg".split(" ")
		val value = CmdLineParser.isValid(args)
		assertThat(value).isEqualTo(true)
	}

	@Test
	fun test22(){
		val args = "--output foo.svg foo.mbx".split(" ")
		val value = CmdLineParser.isValid(args)
		assertThat(value).isEqualTo(true)
	}

	@Test
	fun test23(){
		val args = "foo.mbx foo.svg".split(" ")
		val value = CmdLineParser.isValid(args)
		assertThat(value).isEqualTo(false)
	}

	@Test
	fun test24(){
		val args = "-o foo.svg".split(" ")
		val value = CmdLineParser.isValid(args)
		assertThat(value).isEqualTo(false)
	}
}
