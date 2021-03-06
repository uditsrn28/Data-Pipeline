package data.pipeline.entity.converter

import data.pipeline.entity.SummaryData.SummaryData

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson._

object SummaryConverter {

  implicit object SummaryConverterReader extends BSONDocumentReader[SummaryData] {
    def read(doc: BSONDocument): SummaryData = {
        SummaryData(
          doc.getAs[String]("name").get,
          doc.getAs[Long]("start_date").get,
          doc.getAs[Long]("active_duration").get,
          doc.getAs[List[Int]]("scores").get,
          doc.getAs[List[Int]]("durations").get,
          doc.getAs[List[Long]]("dates").get,
          doc.getAs[Double]("best_score").get,
          doc.getAs[Double]("worst_score").get,
          doc.getAs[Double]("mean_score").get,
          doc.getAs[Int]("mean_duration").get,
          doc.getAs[Double]("std_score").get,
          doc.getAs[Int]("play_count").get,
          doc.getAs[Double]("best_sub_mean_count").get,
          doc.getAs[Double]("best_sub_mean_ratio").get,
          doc.getAs[Int]("total_wins").get,
          doc.getAs[Long]("last_game_play").get,
          doc.getAs[Int]("churned")
        )
      }
    }

}
