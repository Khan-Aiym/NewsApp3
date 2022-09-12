package com.example.homework41.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Model : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var create: Long = 0
    var title: String = "gosha"

    constructor(create: Long, title: String) {
        this.create = create
        this.title = title
    }

    constructor() {}
}