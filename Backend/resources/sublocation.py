from models.sublocation import SubLocationModel
from flask_restful import Resource, reqparse
from flask import request

parser = reqparse.RequestParser()
parser.add_argument('id')
parser.add_argument('locationId')
parser.add_argument('name')
parser.add_argument('imgData')

class CreateSubLocation(Resource):
    def post(self):
        data = parser.parse_args()
        
        new_item = SubLocationModel(
            location_id = data['locationId'],
            name = data['name'],
            img_data = data['imgData']
        )
        try:
            new_item.save_to_db()
            return { 'message': 'created!' }, 200
        except Exception as e:
            return {'message': str(e)}, 400

class ReadSubLocation(Resource):
    def get(self):
        return SubLocationModel.return_all_by_id(request.args.get('id'))
    
class DeleteSubLocation(Resource):
    def delete(self):
        return SubLocationModel.delete_one(request.args.get('id'))
    
class UpdateSubLocation(Resource):
    def put(self):
        data = parser.parse_args()
        return SubLocationModel.update_one(request.args.get('id'), data['name'])
    
class ReadAllLocation(Resource):
    def get(self):
        return SubLocationModel.return_all()