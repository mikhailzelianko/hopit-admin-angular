import { ITrailPath } from 'app/entities/trail-path/trail-path.model';
import { IAdditionalMapObject } from 'app/entities/additional-map-object/additional-map-object.model';
import { ICountry } from 'app/entities/country/country.model';
import { IRegion } from 'app/entities/region/region.model';
import { IDistrict } from 'app/entities/district/district.model';
import { TrailType } from 'app/entities/enumerations/trail-type.model';

export interface ITrail {
  id?: number;
  title?: string;
  description?: string | null;
  shortDescription?: string | null;
  specialRules?: string | null;
  type?: TrailType;
  coverPhotoContentType?: string | null;
  coverPhoto?: string | null;
  price?: number | null;
  enterLat?: number | null;
  enterLong?: number | null;
  flagUnavailable?: boolean | null;
  flagWater?: boolean | null;
  flagBirdwatching?: boolean | null;
  flagReligious?: boolean | null;
  flagFishing?: boolean | null;
  flagParking?: boolean | null;
  flagWC?: boolean | null;
  flagCamping?: boolean | null;
  flagPicnic?: boolean | null;
  flagStreetfood?: boolean | null;
  source?: string | null;
  adminComment?: string | null;
  trailPaths?: ITrailPath[] | null;
  additionalMapObjects?: IAdditionalMapObject[] | null;
  country?: ICountry | null;
  region?: IRegion | null;
  district?: IDistrict | null;
}

export class Trail implements ITrail {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public shortDescription?: string | null,
    public specialRules?: string | null,
    public type?: TrailType,
    public coverPhotoContentType?: string | null,
    public coverPhoto?: string | null,
    public price?: number | null,
    public enterLat?: number | null,
    public enterLong?: number | null,
    public flagUnavailable?: boolean | null,
    public flagWater?: boolean | null,
    public flagBirdwatching?: boolean | null,
    public flagReligious?: boolean | null,
    public flagFishing?: boolean | null,
    public flagParking?: boolean | null,
    public flagWC?: boolean | null,
    public flagCamping?: boolean | null,
    public flagPicnic?: boolean | null,
    public flagStreetfood?: boolean | null,
    public source?: string | null,
    public adminComment?: string | null,
    public trailPaths?: ITrailPath[] | null,
    public additionalMapObjects?: IAdditionalMapObject[] | null,
    public country?: ICountry | null,
    public region?: IRegion | null,
    public district?: IDistrict | null
  ) {
    this.flagUnavailable = this.flagUnavailable ?? false;
    this.flagWater = this.flagWater ?? false;
    this.flagBirdwatching = this.flagBirdwatching ?? false;
    this.flagReligious = this.flagReligious ?? false;
    this.flagFishing = this.flagFishing ?? false;
    this.flagParking = this.flagParking ?? false;
    this.flagWC = this.flagWC ?? false;
    this.flagCamping = this.flagCamping ?? false;
    this.flagPicnic = this.flagPicnic ?? false;
    this.flagStreetfood = this.flagStreetfood ?? false;
  }
}

export function getTrailIdentifier(trail: ITrail): number | undefined {
  return trail.id;
}
