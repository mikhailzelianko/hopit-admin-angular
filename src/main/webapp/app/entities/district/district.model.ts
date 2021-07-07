import { IRegion } from 'app/entities/region/region.model';

export interface IDistrict {
  id?: number;
  districtName?: string;
  region?: IRegion | null;
}

export class District implements IDistrict {
  constructor(public id?: number, public districtName?: string, public region?: IRegion | null) {}
}

export function getDistrictIdentifier(district: IDistrict): number | undefined {
  return district.id;
}
