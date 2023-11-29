import Layout from "../../Layout"
import BuildList from "./BuildList"
import InputBuild from "./InputBuild"

const BuildingScreen = () => {
  return (
    <Layout type="base">
      <InputBuild />
      <BuildList />
    </Layout>
  )
}

export default BuildingScreen
