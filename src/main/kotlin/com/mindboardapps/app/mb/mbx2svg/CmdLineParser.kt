package com.mindboardapps.app.mb.mbx2svg

object CmdLineParser {
    fun isValid(args: List<String>): Boolean {
        val inputOptionValue  = toInputOptionValue(args)
        val outputOptionValue = toOutputOptionValue(args)
        return ( inputOptionValue!=null && outputOptionValue!=null)

        /*
        return (
            ( inputOptionValue!=null && outputOptionValue!=null)
            &&
            ( inputOptionValue.endsWith("mbx") && outputOptionValue.endsWith("svg") )
        )
        */
    }

    private val head: (List<String>)->String = { it.first() }
    private val tail: (List<String>)->List<String> = { it.drop(1) }

    private fun parseOutputOption(args: List<String>, acc: MutableList<String>): MutableList<String>{
        return if( args.size<1 ){
            acc
        } else {
            val head = head(args)
            val tail = tail(args)

            if( (head == "-o" || head == "--output") && tail.size>0 ){
                acc.add(tail.first())
                if( tail.size<1 ){
                    acc
                } else {
                    val tail2 = tail(tail)
                    parseOutputOption(tail2, acc)
                }
            } else {
                parseOutputOption(tail, acc)
            }
        }
    }

    fun toOutputOptionValue(args: List<String>): String? {
        val acc = parseOutputOption(args, mutableListOf<String>())
        return if( acc.size>0 ){ acc.first() } else { null }
    }

    private fun parseInputOption(args: List<String>, acc: MutableList<String>): MutableList<String>{
        return if( args.size<1 ){
            acc
        } else {
            val head = head(args)
            val tail = tail(args)
            if( !head.startsWith("-") ){
                acc.add(head)
                parseInputOption(tail, acc)
            } else {
                if(tail.size<1){
                    acc
                } else {
                    val tail2 = tail(tail)
                    parseInputOption(tail2, acc)
                }
            }
        }
    }

    fun toInputOptionValue(args: List<String>): String? {
        val acc = parseInputOption(args, mutableListOf<String>())
        return if( acc.size>0 ){ acc.first() } else { null }
    }
}
