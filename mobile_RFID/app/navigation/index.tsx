import * as React from "react"
import { NavigationContainer } from "@react-navigation/native"
import { createNativeStackNavigator } from "@react-navigation/native-stack"
import LogIn from "../screens/auth/login"
import Category from "../screens/category"
import Item from "../screens/Item"
import Location from "../screens/location/location"
import SubLocation from "../screens/location/sublocation"
import Inventory from "../screens/inventory/inventory"
import Building from "../screens/building"
import Area from "../screens/area"
import Floor from "../screens/floor"

const RootStack = createNativeStackNavigator()

const RootNavigator = () => {
  return (
    <NavigationContainer>
      <RootStack.Navigator initialRouteName="LogIn">
        <RootStack.Group screenOptions={{ headerShown: false }}>
          <RootStack.Screen name="LogIn" component={LogIn} />
        </RootStack.Group>
        <RootStack.Group screenOptions={{ headerShown: false }}>
          <RootStack.Screen name="Category" component={Category} />
          <RootStack.Screen name="Item" component={Item} />
          <RootStack.Screen name="Building" component={Building} />
          <RootStack.Screen name="Area" component={Area} />
          <RootStack.Screen name="Floor" component={Floor} />
        </RootStack.Group>
        <RootStack.Group screenOptions={{ headerShown: false }}>
          <RootStack.Screen name="DetailLocation" component={Location} />
          <RootStack.Screen name="SubLocation" component={SubLocation} />
        </RootStack.Group>
        <RootStack.Group screenOptions={{ headerShown: false }}>
          <RootStack.Screen name="Inventory" component={Inventory} />
        </RootStack.Group>
      </RootStack.Navigator>
    </NavigationContainer>
  )
}

export default RootNavigator
