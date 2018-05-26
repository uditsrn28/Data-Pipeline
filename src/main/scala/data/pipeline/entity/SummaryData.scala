package data.pipeline.entity.SummaryData

case class SummaryData(
                        start_date: Long,
                        active_duration: Long,
                        scores: List[Int],
                        durations: List[Int],
                        dates: List[Long],
                        best_score: Double,
                        worst_score: Double,
                        mean_score: Double,
                        mean_duration: Int,
                        std_score: Double,
                        play_count: Int,
                        best_sub_mean_count: Double,
                        best_sub_mean_ratio: Double,
                        total_wins: Int,
                        last_game_play: Long,
                        churned: Option[Int]
                  )
