package data.pipeline.entity.converter

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson._

object SummaryConverter {

  implicit object SummaryConverterReader extends BSONDocumentReader[SummaryData] {
    best_sub_mean_ratio: Float,
    total_wins: Int,
    last_game_play: Long,
    churned: Int
    def read(doc: BSONDocument): SummaryData = {
        SummaryData(
          doc.getAs[Long]("start_date").get,
          doc.getAs[Int]("active_duration").get,
          doc.getAs[List[Int]]("scores").get,
          doc.getAs[List[Int]]("durations").get,
          doc.getAs[List[Long]]("dates").get,
          doc.getAs[Float]("best_score").get,
          doc.getAs[Float]("worst_score").get,
          doc.getAs[Float]("mean_score").get,
          doc.getAs[Int]("mean_duration").get,
          doc.getAs[Float]("std_score").get,
          doc.getAs[Int]("play_count").get,
          doc.getAs[Float]("best_sub_mean_count").get,
          doc.getAs[Float]("best_sub_mean_ratio").get,
          doc.getAs[Int]("total_wins").get,
          doc.getAs[Long]("last_game_play").get,
          doc.getAs[Int]("churned").getOrElse(None),
        )
      }
    }

}
