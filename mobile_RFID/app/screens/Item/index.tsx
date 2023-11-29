import ItemScreen from "../../components/Screens/ItemScreen";
import ItemProvider from "../../providers/ItemProvider";

const Item = () => <ItemProvider>
    <ItemScreen />
</ItemProvider>

export default Item