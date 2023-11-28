from models.location import LocationModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('name')

class CreateLocation(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = LocationModel(
            name = data['name']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'created!' }, 200
        except:
            return {'message': 'Not created!'}, 400

class ReadLocation(Resource):
    def get(self):
        return LocationModel.return_all()
    
class DeleteLocation(Resource):
    def delete(self):
        return LocationModel.delete_one(request.args.get('id'))
    
class UpdateLocation(Resource):
    def put(self):
        data = parser.parse_args()
        return LocationModel.update_one(request.args.get('id'), data['name']) 