package com.surendra.sparkcore

import org.apache.spark.sql.SparkSession


object MinMaxPerCreditcard {

  def main(args: Array[String]) {

    val masterOfCluster = args(0)
    val inputPath = args(1)
    //val outputPath = args(2)

    val sparkSession = SparkSession
      .builder()
      .master(masterOfCluster)
      .appName("Load Credit card data")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val dataRdd = sparkSession.sparkContext.textFile(inputPath)

    val transactionRdd = dataRdd.map(CreditcardTransaction.parse)

    val pairRdd = transactionRdd.map(transaction => (transaction.cc_num, transaction.amt)).cache()

    val maxPerCreditcard = pairRdd.reduceByKey(Math.max(_, _))
    val minPerCreditcard = pairRdd.reduceByKey(Math.min(_, _))

    maxPerCreditcard.foreach(println)
    minPerCreditcard.foreach(println)

  }
}