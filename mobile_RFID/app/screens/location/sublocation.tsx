import React, { useState, useEffect } from "react"
import { StyleSheet, TouchableOpacity, View, PermissionsAndroid, Image } from "react-native"
import { useQuery } from "react-query"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"

import { useAppSelector } from "../../store/hooks"
import useSubLocationForm from "../../components/location/useSubLocationForm"
import ContainerLayout from "../../components/Layout/main"
import http from "../../service/http-common"
import _Input from "../../shared/core/ui/_Input"
import _Button from "../../shared/core/ui/_Button"
import _Text from "../../shared/core/ui/_Text"

import { launchCamera, CameraOptions } from "react-native-image-picker"
import { showToastWithGravity } from "../../shared/helper/toast"

const SubLocation = ({ navigation }: any) => {
  const routes = navigation.getState().routes
  const { location_id: id, location_name } = routes[routes.length - 1].params
  const { create, delete: deleteOne, update } = useSubLocationForm()
  const [name, setName] = useState<string>("")
  const [curId, setCurId] = useState(-1)
  const [img, setImg] = useState<any>()
  const [imgData, setImgData] = useState<any>()
  const { account } = useAppSelector((state) => state.auth)

  const getAllSubLocation: any = useQuery(["getAllSubLocation", id], () => {
    if (id) return http.get(`/sublocation/read?id=${id}`)
  })

  const allSubLocation = React.useMemo(() => {
    return getAllSubLocation.data ? getAllSubLocation.data.data : []
  }, [getAllSubLocation.data])

  const handleAdd = () => {
    if (name == "" || name == undefined) {
      showToastWithGravity("Input field should not be empty")
      return
    }
    create({ locationId: id, name, imgData: imgData })
    setName("")
  }

  const handleDelete = (id: string) => {
    deleteOne(id)
  }

  const handleEdit = (id: number, value: string) => {
    setCurId(id)
    setName(value)
  }

  const handleUpdate = () => {
    update({ id: curId, name })
    setCurId(-1)
    setName("")
  }

  const handleCancel = () => {
    setCurId(-1)
    setName("")
  }

  const handleTakePicture = async () => {
    const options: CameraOptions = { mediaType: "photo" }

    await launchCamera(options, async (response) => {
      if (response.didCancel) {
        console.log("User cancelled image picker")
      } else {
        if (response.assets) {
          setImg(response.assets[0])
        }
      }
    })
  }

  const requestCameraPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA, {
        title: "App Camera Permission",
        message: "App needs access to your camera ",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK",
      })
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log("Camera permission given")
      } else {
        console.log("Camera permission denied")
      }
    } catch (err) {
      console.warn(err)
    }
  }

  const convertImageToBase64 = async (img: any) => {
    if (img?.uri == undefined) return

    try {
      const response = await fetch(img.uri)
      const blob = await response.blob()

      const reader = new FileReader()
      reader.onloadend = () => {
        if (typeof reader.result == "string") {
          const base64Image = reader.result.split(",")[1]

          // Now you can send the base64Image to your backend
          // (e.g., using a network request or any other suitable method)

          setImgData(`data:image/png;base64,${base64Image}`)
        }
      }

      reader.readAsDataURL(blob)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    requestCameraPermission()
  }, [])

  useEffect(() => {
    convertImageToBase64(img)
  }, [img])

  return (
    <ContainerLayout navigation={navigation}>
      <View style={styles.container}>
        <View
          style={{
            marginVertical: 10,
            flexDirection: "row",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <_Text text={location_name} size={20} lineHeight={20} />
          <TouchableOpacity onPress={handleTakePicture}>
            <AntDesign name="camera" size={48} color={"indigo"} />
          </TouchableOpacity>
        </View>
        <View style={styles.subheader}>
          {account?.role != 3 && <_Input width={"60%"} value={name} setValue={setName} />}
          {account?.role != 3 &&
            (curId != -1 ? (
              <>
                <_Button
                  text="Update"
                  px={10}
                  py={10}
                  onPress={handleUpdate}
                  borderRadius={10}
                  filled
                />
                <_Button
                  text="Cancel"
                  px={10}
                  py={10}
                  onPress={handleCancel}
                  borderRadius={10}
                  filled
                />
              </>
            ) : (
              <_Button text="ADD" px={20} py={10} onPress={handleAdd} borderRadius={10} filled />
            ))}
        </View>
        <View style={styles.main}>
          {allSubLocation?.map((item: any, index: number) => (
            <View
              key={index}
              style={[styles.item, { backgroundColor: !item.isUsed ? "blue" : "red" }]}
            >
              <TouchableOpacity style={styles.nameBoard} onPress={() => setImgData(item.imgData)}>
                <_Text text={item.name} color="white" />
              </TouchableOpacity>
              {curId != item.id && account?.role != 3 && (
                <TouchableOpacity
                  style={styles.editBoard}
                  onPress={() => handleEdit(item.id, item.name)}
                >
                  <Entypo name="edit" color="white" />
                </TouchableOpacity>
              )}
              {account?.role != 3 && (
                <TouchableOpacity style={styles.deleteBoard} onPress={() => handleDelete(item.id)}>
                  <AntDesign name="delete" color="white" />
                </TouchableOpacity>
              )}
            </View>
          ))}
        </View>
        {imgData != undefined && (
          <View style={styles.cameraBoard}>
            <Image
              source={{ uri: imgData }}
              style={styles.importedphoto}
              width={img?.width == undefined ? 300 : (img?.width * 300) / img?.height}
            />
          </View>
        )}
      </View>
    </ContainerLayout>
  )
}

const styles = StyleSheet.create({
  container: {
    paddingVertical: "3%",
    paddingHorizontal: "2%",
    width: "100%",
  },
  subheader: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 10,
  },
  main: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 10,
    marginTop: 20,
  },
  item: {
    color: "white",
    borderRadius: 5,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  nameBoard: {
    paddingHorizontal: 12,
    paddingVertical: 4,
  },
  editBoard: {
    paddingRight: 6,
    paddingVertical: 4,
  },
  deleteBoard: {
    paddingRight: 6,
    paddingVertical: 4,
  },
  cameraBoard: {
    borderWidth: 1,
    borderColor: "#ccc",
    marginTop: 30,
    height: 300,
    borderRadius: 20,
    alignItems: "center",
  },
  importedphoto: {
    height: "100%",
    borderRadius: 20,

    borderWidth: 1,
    borderColor: "white",
    justifyContent: "center",
    alignItems: "center",
  },
})

export default SubLocation
