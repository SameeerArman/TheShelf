package com.sam.theshelf

class ModelBook {

    var id:String = ""
    var title:String = ""
    var score:String = ""
    var popularity:String= ""
    var image:String = ""
    var publishedChapterDate:String = ""
    var uid:String = ""
    var desc:String = ""

    constructor()

    constructor(id:String,title:String,score:String,popularity:String,image:String,publishedChapterDate:String,uid:String, desc:String){

        this.id = id
        this.title = title
        this.score = score
        this.popularity = popularity
        this.image = image
        this.publishedChapterDate = publishedChapterDate
        this.uid = uid
        this.desc = desc
    }




}