package com.wlp.ibolletta.util

import java.util.regex.Pattern

class ParseORCBolletta(val toParseString: String) {

    lateinit var numero : String
    lateinit var cc : String
    lateinit var importo : String
    lateinit var td : String


    fun parseNumberBarCode()
    {
        var numberBarCode = ""

        val cleanString = Regex("[^A-Za-z0-9]").replace(toParseString," ")
        var pat = Pattern.compile("""\d{50}""").matcher(cleanString)

        if (pat.find()) {
            numberBarCode =  pat.group(0)
        }
        else{


            if(Pattern.compile("""18\d{5}""").matcher(cleanString).find())
                pat = Pattern.compile("""18\d{5}""").matcher(cleanString)
            else if(Pattern.compile("""1 8\d{5}""").matcher(cleanString).find())
                pat = Pattern.compile("""1 8\d{5}""").matcher(cleanString)
            else if(Pattern.compile("""1 8 \d{5}""").matcher(cleanString).find())
                pat = Pattern.compile("""1 8 \d{5}""").matcher(cleanString)
            else
                pat = Pattern.compile("""18 \d{5}""").matcher(cleanString)

                    if (pat.find()) {
                numberBarCode =  pat.group(0)
                numberBarCode = cleanString.substring(cleanString.replace(" ","").indexOf(numberBarCode)
                    , cleanString.replace(" ","").indexOf(numberBarCode) + 50)
            }
        }

        if(!numberBarCode.isBlank()){
            numero  = numberBarCode.substring(2, 20)
            cc      = numberBarCode.substring(22, 34)
            importo = numberBarCode.substring(36, 46)
            importo = "${importo.substring(0, importo.length - 2)},${importo.substring(importo.length - 2, importo.length)}"
            td      = numberBarCode.substring(47, 50)
        }
        else{
            numero  = ""
            cc      = ""
            importo = ""
            td      = ""
        }
    }


    fun getScadenza(): String? {

        return try {
            val pat = Pattern.compile("""\d{2}\/\d{2}\/\d{4}""").matcher(toParseString)

            if (pat.find()) {
                return pat.group(0)
            }

            return ""

        } catch (e: Exception) {
            ""
        }
    }

}