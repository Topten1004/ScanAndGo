from flask_restful import Api
import resources.building as BuildingResource

def buildingRouteIndex(app):
    api = Api(app)
    
    api.add_resource(BuildingResource.CreateBuilding, '/api/building/create')
    api.add_resource(BuildingResource.ReadBuilding, '/api/building/read')
    api.add_resource(BuildingResource.UpdateBuilding, '/api/building/update')
    api.add_resource(BuildingResource.DeleteBuilding, '/api/building/delete')