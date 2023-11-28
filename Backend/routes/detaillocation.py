from flask_restful import Api
import resources.detailLocation as DetailLocationResource

def detailLocationRouteIndex(app):
    api = Api(app)
    
    api.add_resource(DetailLocationResource.CreateDetailLocation, '/api/detaillocation/create')
    api.add_resource(DetailLocationResource.ReadDetailLocation, '/api/detaillocation/read')
    api.add_resource(DetailLocationResource.UpdateDetailLocation, '/api/detaillocation/update')
    api.add_resource(DetailLocationResource.DeleteDetailLocation, '/api/detaillocation/delete')
    api.add_resource(DetailLocationResource.ReadAllDetailLocation, '/api/detaillocation/readall')