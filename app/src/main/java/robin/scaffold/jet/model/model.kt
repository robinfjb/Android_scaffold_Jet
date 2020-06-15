package robin.scaffold.jet.model

data class FlexModel(val name: String,
                val isTag: Boolean = false,
                val count: Int = 0,
                val content: String = "",
                val extra: String = "")