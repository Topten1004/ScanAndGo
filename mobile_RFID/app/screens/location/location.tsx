import DetailLocationScreen from "../../components/Screens/DetailLocationScreen"
import DetailLocationProvider from "../../providers/DetailLocationProvider"
import LCIProvider from "../../providers/LCIProvider"

const Location = () => (
  <LCIProvider>
    <DetailLocationProvider>
      <DetailLocationScreen />
    </DetailLocationProvider>
  </LCIProvider>
)

export default Location
