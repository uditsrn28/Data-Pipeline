package data.pipeline.entity.logData

case class LogData(
                    auth: Boolean,
                    degree: Int,
                    events: String,
                    flair: Int,
                    `match`: Int,
                    name: String,
                    points: Int,
                    score: Int,
                    team: Int,
                    `0_teams_name`: String,
                    `0_teams_score`: Int,
                    `0_teams_splats`: String,
                    `1_teams_name`: String,
                    `1_teams_score`: Int,
                    `1_teams_splats`: String,
                    date: Long,
                    duration: Int,
                    group: Option[Int],
                    mapId: Int,
                    official: Boolean,
                    port: Int,
                    server: String,
                    timeLimit: Int
                  )