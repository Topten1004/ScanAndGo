import BuildingScreen from "../../components/Screens/BuildingScreen"
import BuildingProvider from "../../providers/BuildingProvider"

const Building = () => (
  <BuildingProvider>
    <BuildingScreen />
  </BuildingProvider>
)

export default Building
