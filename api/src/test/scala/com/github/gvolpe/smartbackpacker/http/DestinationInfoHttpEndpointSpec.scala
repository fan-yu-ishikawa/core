package com.github.gvolpe.smartbackpacker.http

import cats.effect.IO
import com.github.gvolpe.smartbackpacker.{TestExchangeRateService, TestWikiPageParser}
import com.github.gvolpe.smartbackpacker.service.CountryService
import org.http4s.{HttpService, Query, Request, Status, Uri}
import org.scalatest.{FlatSpecLike, Matchers}
import ResponseBodyUtils._
import org.scalatest.prop.PropertyChecks

class DestinationInfoHttpEndpointSpec extends FlatSpecLike with Matchers with DestinationInfoHttpEndpointFixture {

  val httpService: HttpService[IO] = new DestinationInfoHttpEndpoint(
    new CountryService[IO](TestWikiPageParser, TestExchangeRateService)
  ).service

  forAll(examples) { (from, to, expectedStatus, expectedCountry, expectedVisa) =>
    it should s"retrieve visa requirements from $from to $to" in {
      val a = Uri.unsafeFromString(s"/traveling/$from/to/$to?baseCurrency=EUR")

      val request = Request[IO](uri = Uri(path = s"/traveling/$from/to/$to", query = Query(("baseCurrency", Some("EUR")))))

      val task = httpService(request).value.unsafeRunSync()
      task should not be None
      task foreach { response =>
        response.status should be (expectedStatus)

        val body = response.body.asString
        assert(body.contains(expectedCountry))
        assert(body.contains(expectedVisa))
      }
    }
  }

}

trait DestinationInfoHttpEndpointFixture extends PropertyChecks {

  val examples = Table(
    ("from", "code", "expectedStatus","expectedCountry", "expectedVisa"),
    ("AR", "GB", Status.Ok, "United Kingdom", "VisaNotRequired"),
    ("CA", "SO", Status.Ok, "Somalia", "VisaRequired"),
    ("AR", "KO", Status.BadRequest, "Country code not found", "")
  )

}