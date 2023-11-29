import { View } from "react-native"
import styles from "./style"
import Input from "../../../../shared/core/Input"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"

const CommentInput = () => {
    const { comment, setComment } = useDetailLocation()
    return (
        <View style={styles.inputView}>
            <Input placeholder="Enter the comment." value={comment} setValue={setComment} width={200} multiline />
        </View>
    )
}

export default CommentInput