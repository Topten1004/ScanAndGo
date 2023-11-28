from flask_restful import Api
import resources.user as userResource

def userRouteIndex(app):
    api = Api(app)
    
    api.add_resource(userResource.UserRegister, '/api/user/register')
    api.add_resource(userResource.UserLogin, '/api/user/signin')
    api.add_resource(userResource.AllUsers, '/api/user/alluser')
    api.add_resource(userResource.UpdateUser, '/api/user/update')
    api.add_resource(userResource.DeleteUser, '/api/user/delete')
