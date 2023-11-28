from flask_restful import Api
import resources.item as ItemResource

def itemRouteIndex(app):
    api = Api(app)
    
    api.add_resource(ItemResource.CreateItem, '/api/item/create')
    api.add_resource(ItemResource.ReadItem, '/api/item/read')
    api.add_resource(ItemResource.UpdateItem, '/api/item/update')
    api.add_resource(ItemResource.DeleteItem, '/api/item/delete')
    api.add_resource(ItemResource.ReadAllItem, '/api/item/readall')
    api.add_resource(ItemResource.AssignBarcode, '/api/item/assign-barcode')