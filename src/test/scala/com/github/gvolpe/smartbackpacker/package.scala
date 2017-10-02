package com.github.gvolpe

import cats.effect.IO
import com.github.gvolpe.smartbackpacker.model.CountryCode
import com.github.gvolpe.smartbackpacker.parser.AbstractWikiPageParser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

import scala.io.Source

package object smartbackpacker {

  object TestWikiPageParser extends AbstractWikiPageParser[IO] {
    override def htmlDocument(from: CountryCode): Document = {
      val browser = JsoupBrowser()
      val fileContent = Source.fromResource("wikiPageTest.html").mkString
      browser.parseString(fileContent).asInstanceOf[Document]
    }
  }

}
