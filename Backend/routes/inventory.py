from flask_restful import Api
import resources.inventory as InventoryResource

def inventoryRouteIndex(app):
    api = Api(app)
    
    api.add_resource(InventoryResource.CreateInventory, '/api/inventory/create')
    api.add_resource(InventoryResource.CreateNewInventory, '/api/inventory/create/new')
    api.add_resource(InventoryResource.ReadInventory, '/api/inventory/readall')
    api.add_resource(InventoryResource.ReadByDetailLocation, '/api/inventory/detaillocation/read')
    api.add_resource(InventoryResource.UpdateInventory, '/api/inventory/update')