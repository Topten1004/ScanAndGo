import React, { useState } from "react"
import { StyleSheet, TouchableOpacity, View } from "react-native"
import { useQuery } from "react-query"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"

import { useAppSelector } from "../../store/hooks"
import useItemForm from "../../components/Item/useItemForm"
import ContainerLayout from "../../components/Layout/main"
import http from "../../service/http-common"
import _Input from "../../shared/core/ui/_Input"
import _Button from "../../shared/core/ui/_Button"
import _Text from "../../shared/core/ui/_Text"

const Item = ({ navigation }: any) => {
  const routes = navigation.getState().routes
  const {
    category_id: id,
    category_name,
    subcategory_name,
    subcategory_id: subId,
  } = routes[routes.length - 1].params
  const { create, delete: deleteOne, update } = useItemForm()
  const [name, setName] = useState<string>("")
  const [curId, setCurId] = useState(-1)
  const { account } = useAppSelector((state) => state.auth)

  const getAllItem: any = useQuery(["getAllItem", subId], () => {
    if (subId) return http.get(`/item/read?id=${subId}`)
  })

  const allItem = React.useMemo(() => {
    return getAllItem.data ? getAllItem.data.data : []
  }, [getAllItem.data])

  const handleAdd = () => {
    create({ subCategoryId: subId, name })
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

  return (
    <ContainerLayout navigation={navigation}>
      <View style={styles.container}>
        <View style={styles.tag}>
          <TouchableOpacity
            onPress={() =>
              navigation.navigate("SubCategory", {
                category_name,
                category_id: id,
              })
            }
          >
            <_Text text={category_name} size={20} lineHeight={20} />
          </TouchableOpacity>
          <_Text text=" / " />
          <_Text text={subcategory_name} size={20} lineHeight={20} />
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
          {allItem?.map((item: any, index: number) => (
            <View
              key={index}
              style={[styles.item, { backgroundColor: !item.isUsed ? "blue" : "red" }]}
            >
              <TouchableOpacity style={styles.nameBoard}>
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
              {!item.isUsed && account?.role != 3 && (
                <TouchableOpacity style={styles.deleteBoard} onPress={() => handleDelete(item.id)}>
                  <AntDesign name="delete" color="white" />
                </TouchableOpacity>
              )}
            </View>
          ))}
        </View>
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
  tag: {
    paddingVertical: 10,
    flexDirection: "row",
    alignItems: "center",
  },
})

export default Item
