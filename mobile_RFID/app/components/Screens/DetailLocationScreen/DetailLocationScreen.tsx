import Layout from "../../Layout"
import LocationList from "./LocationList"
import InputLocation from "./InputLocation"
import { useDetailLocation } from "../../../providers/DetailLocationProvider"
import CapturedImage from "./CapturedImage"
import BarCodeInput from "./BarCodeInpput"
import Evaluates from "./Evaluates"
import { STEPS } from "../../../consts/detailStep"
import CategorySelect from "./CategorySelect"
import ItemList from "./ItemList"
import CommentInput from "./CommentInput"

const DetailLocationScreen = () => {
  const { capturedImg, currentStep } = useDetailLocation()

  return (
    <Layout type="base">
      {!capturedImg && (
        <>
          <InputLocation />
          <LocationList />
        </>
      )}
      {capturedImg && currentStep === STEPS.SELECT_CATEGORY_ITEM && (
        <>
          <CategorySelect />
          <ItemList />
        </>
      )}
      {capturedImg && currentStep === STEPS.INPUT_BAR_CODE && (
        <>
          <BarCodeInput />
          <CommentInput />
          <CapturedImage />
          <Evaluates />
        </>
      )}
    </Layout>
  )
}

export default DetailLocationScreen
