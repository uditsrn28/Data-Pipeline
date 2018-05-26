package data.pipeline.entity.SummaryData

case class SummaryData(
                        start_date: Long,
                        active_duration: Int,
                        scores: List[Int],
                        durations: List[Int],
                        dates: List[Long],
                        best_score: Float,
                        worst_score: Float,
                        mean_score: Float,
                        mean_duration: Int,
                        std_score: Float,
                        play_count: Int,
                        best_sub_mean_count: Float,
                        best_sub_mean_ratio: Float,
                        total_wins: Int,
                        last_game_play: Long,
                        churned: Option[Int]
                  )
