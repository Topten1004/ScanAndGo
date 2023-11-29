import { View } from "react-native"
import styles from "./styles"
import LocationItem from "../LocationItem"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import { ILocation } from "../../../../types/location"

const LocationList = () => {
  const { locations } = useDetailLocation()

  return (
    <View style={styles.container}>
      {locations.map((location: ILocation) => (
        <LocationItem key={location.id} data={location} />
      ))}
    </View>
  )
}

export default LocationList
