package controllers

import org.mockito.Mockito._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import model.repository._
import play.api.Play.current
import play.api.cache.Cache
import play.api.test.FakeApplication
import play.api.test.Helpers._
import play.api.test.FakeRequest
import play.api.mvc.Security
import utils._
import play.api.mvc.Security
import java.util.Date
import java.util.Calendar
import java.sql.Timestamp

class AuditControllerTestCases extends Specification with Mockito {

  val timestamp = Audit("864465028854206", new Timestamp(new java.util.Date().getTime))
  val auditList=List(Audit("864465028854206", new Timestamp(new java.util.Date().getTime),Some(1)))
  val mobileUser = Mobile(
    "sushil", 1, 1, "864465028854206", "123456789012677", CommonUtils.getSqlDate(), "+91 9839839830",
    "gs@gmail.com", "stolen", StatusUtil.Status.pending, "test", CommonUtils.getSqlDate(), "gaurav.png", "nokia", "E5")
  val user = User("admin", "knol2013")
  val mockedAudit = mock[AuditRepository]

  val auditController = new AuditController(mockedAudit)

  "AuditControllerTesting: auditPage" in {
    running(FakeApplication()) {
      Cache.set("admin", user)
      val result = auditController.auditPage(FakeRequest().withSession(Security.username -> "admin"))
      status(result) must equalTo(200)
    }
  }
  
  "AuditControllerTesting: timestampsByIMEI-> with valid form data" in {
    running(FakeApplication()) {
      Cache.set("admin", user)
      when(mockedAudit.getAllTimestampsByIMEID("864465028854206")).thenReturn(auditList)
      val result = auditController.timestampsByIMEI(FakeRequest().withFormUrlEncodedBody("imeiMeid"->"864465028854206").withSession(Security.username -> "admin"))
      status(result) must equalTo(200)
    }
  }
  
  "AuditControllerTesting: timestampsByIMEI-> with invalid form data" in {
    running(FakeApplication()) {
      Cache.set("admin", user)
      val result = auditController.timestampsByIMEI(FakeRequest().withSession(Security.username -> "admin"))
      status(result) must equalTo(200)
    }
  }
  
  "AuditControllerTesting: allTimestamps" in {
    running(FakeApplication()) {
      Cache.set("admin", user)
      when(mockedAudit.getAllTimestamps).thenReturn(auditList)
      val result = auditController.allTimestamps(FakeRequest().withSession(Security.username -> "admin"))
      status(result) must equalTo(200)
    }
  }
  
  "AuditControllerTesting: registrationRecordsByYear" in {
    running(FakeApplication()) {
      Cache.set("admin", user)
      when(mockedAudit.insertMobileUser(mobileUser)).thenReturn(Right(Some(1)))
      when(mockedAudit.getRecordByDate("2015")).thenReturn(List(1))
      val result = auditController.registrationRecordsByYear("2015")(FakeRequest().withSession(Security.username -> "admin"))
      status(result) must equalTo(200)
    }
  }
}
