import Layout from "../../Layout"
import FloorList from "./FloorList"
import InputFloor from "./InputFloor"

const FloorScreen = () => {
  return (
    <Layout type="base">
      <InputFloor />
      <FloorList />
    </Layout>
  )
}

export default FloorScreen
