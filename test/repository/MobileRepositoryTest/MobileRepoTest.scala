package repository.MobileRepositoryTest

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite
import model.repository.Brand
import model.repository.Brand
import model.repository.BrandRepository
import model.repository.Mobile
import model.repository.MobileRepository
import model.repository.Model
import model.repository.ModelRepository
import play.api.test.FakeApplication
import play.api.test.Helpers._
import utils.StatusUtil.Status
import model.repository.Brand
import model.repository.Model

/**
 * Class MobileRepoTest: Unit tests the methods in MobileRepository.
 */

class MobileRepoTest extends FunSuite with BeforeAndAfterEach with MobileRepository with BrandRepository with ModelRepository {

  val brand = Brand("nokia", "12-17-2013")
  val model = Model("N72", 1)
  val mobileUser = Mobile(
    "gauravs", 1, 2, "12345678901234", "12345678902134", "12-05-2013", "+91 9839839830",
    "gs@gmail.com", "stolen", Status.pending, "ddas asd", "12-17-2013", "gaurav.png", "Sigma", "Sigma454", Some(1))

  //Mobile Insertion Test 
  test("MobileRepository:insert and get mobile name successfully ") {
    running(FakeApplication()) {
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      assert(insertedMobile === Right(Some(1)))
    }
  }

  //Tests the insertion of a MobileUser with Duplicate Email
  test("MobileRepository:insert fails since email is duplicate ") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      //Insert it again to test duplicate entry 
      val insertedDuplicateMobile = MobileRepository.insertMobileUser(mobileUser)
      assert(insertedDuplicateMobile.isLeft)
    }
  }

  //Test the fetching of Mobile Record by an IMEID
  test("MobileRepository: get Mobile by IMEID ") {
    running(FakeApplication()) {
      val imeiInserted = "12345678901234"
      MobileRepository.insertMobileUser(mobileUser)
      val insertedMobile = mobileUser
      val mobileUserToCompareWith = MobileRepository.getMobileUserByIMEID(imeiInserted)
      assert(mobileUser === mobileUserToCompareWith.get)
    }
  }

  //Test the status change from Pending to Approved
  test("MobileRepository: change status from Pending to Approved and must return Right(1)") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      //Changes its status
      val returnValueOnChange = MobileRepository.changeStatusToApproveByIMEID(mobileUser)
      assert(returnValueOnChange === Right(1))
    }
  }

  //Test the status change of Mobile from pending to DemandProof
  test("MobileRepository: change of Mobile from pending to DemandProof: must return Right(1)") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      //Changes its status
      val returnValueOnChange = MobileRepository.changeStatusToDemandProofByIMEID(mobileUser)
      assert(returnValueOnChange === Right(1))
    }
  }

  //Test the status change of Mobile mobile registration (stolen or clean) 
  test("MobileRepository: Change registration type (Stolen or Clean): must return Right(1)") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      //Changes its status
      val returnValueOnChange = MobileRepository.changeRegTypeByIMEID(mobileUser)
      assert(returnValueOnChange === Right(1))
    }
  }

  //Test the Retrieval all mobile user with brand and model based on status 
  test("MobileRepository: Retrieval all mobile user with brand and model") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
      //Insert a Brand Record
      BrandRepository.insertBrand(Brand("Sigma","02-02-2015"))      
      //Insert a Model Record
      ModelRepository.insertModel((Model("Sigma45434",1)))
      ModelRepository.insertModel((Model("Sigma454",1)))
      val valueToComapre = List((mobileUser, "Sigma", "Sigma454"))
      val returnValueOnChange = MobileRepository.getAllMobilesUserWithBrandAndModel("pending")
      assert(returnValueOnChange === valueToComapre)
    }
  }

  //Test the deletion of mobile user 
  test("MobileRepository: delete a mobile user record") {
    running(FakeApplication()) {
      //Insert a Mobile Record first
      val insertedMobile = MobileRepository.insertMobileUser(mobileUser)
          ModelRepository.insertModel((Model("Sigma454",1)))
      val valueToComapre = List((mobileUser, "Sigma", "Sigma454"))
      val returnValueOnChange = MobileRepository.getAllMobilesUserWithBrandAndModel("pending")
      assert(returnValueOnChange === valueToComapre)
    }
  }
  
  

}