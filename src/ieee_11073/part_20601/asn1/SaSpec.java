/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package ieee_11073.part_20601.asn1;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net
// Any modifications to this file will be lost upon recompilation of the source ASN.1.
//

import org.bn.*;
import org.bn.annotations.*;
import org.bn.annotations.constraints.*;
import org.bn.coders.*;
import org.bn.types.*;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "SaSpec", isSet = false )
    public class SaSpec implements IASN1PreparedElement {
            @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint (

		min = 0L,

		max = 65535L

	   )

        @ASN1Element ( name = "array-size", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 0  )

	private Integer array_size = null;


        @ASN1Element ( name = "sample-type", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 1  )

	private SampleType sample_type = null;


        @ASN1Element ( name = "flags", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 2  )

	private SaFlags flags = null;



        public Integer getArray_size () {
            return this.array_size;
        }



        public void setArray_size (Integer value) {
            this.array_size = value;
        }



        public SampleType getSample_type () {
            return this.sample_type;
        }



        public void setSample_type (SampleType value) {
            this.sample_type = value;
        }



        public SaFlags getFlags () {
            return this.flags;
        }



        public void setFlags (SaFlags value) {
            this.flags = value;
        }




        public void initWithDefaults() {

        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(SaSpec.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }