from flask_restful import Api
import resources.detaillocation as DetailLocationResource

def detailLocationRouteIndex(app):
    api = Api(app)
    
    api.add_resource(DetailLocationResource.CreateDetailLocation, '/api/detaillocation/create')
    api.add_resource(DetailLocationResource.ReadDetailLocationById, '/api/detaillocation/read')
    api.add_resource(DetailLocationResource.UpdateDetailLocation, '/api/detaillocation/update')
    api.add_resource(DetailLocationResource.DeleteDetailLocation, '/api/detaillocation/delete')
    api.add_resource(DetailLocationResource.ReadDetailLocationByFloor, '/api/detaillocation/readall')