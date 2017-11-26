package com.github.gvolpe.smartbackpacker.service

import com.github.gvolpe.smartbackpacker.TestExchangeRateService
import com.github.gvolpe.smartbackpacker.common.IOAssertion
import com.github.gvolpe.smartbackpacker.model._
import org.scalatest.{FlatSpecLike, Matchers}

class ExchangeServiceSpec extends FlatSpecLike with Matchers {

  it should "retrieve a fake exchange rate" in IOAssertion {
    TestExchangeRateService.exchangeRateFor("EUR".as[Currency], "RON".as[Currency]).map { exchangeRate =>
      exchangeRate should be(CurrencyExchangeDTO("EUR", "", Map("RON" -> 4.59)))
    }
  }

  it should "return an empty exchange rate" in IOAssertion {
    TestExchangeRateService.exchangeRateFor("".as[Currency], "".as[Currency]).map { exchangeRate =>
      exchangeRate should be(CurrencyExchangeDTO("", "", Map("" -> 0.0)))
    }
  }

}
