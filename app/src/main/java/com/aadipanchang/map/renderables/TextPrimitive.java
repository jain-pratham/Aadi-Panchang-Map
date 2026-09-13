// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.aadipanchang.map.renderables;

import com.aadipanchang.map.math.CoordinateManipulationsKt;
import com.aadipanchang.map.math.Vector3;
import com.google.common.base.Preconditions;


/**
 * A Primitive which consists of only a text label (no point will be drawn).
 *
 * @author Brent Bryan
 */
public class TextPrimitive extends AbstractPrimitive {
  public String label;
  public final float offset;
  public final int fontSize;
  public final int outlineColor;
  public final float outlineWidth;

  public TextPrimitive(float ra, float dec, String label, int color) {
    this(CoordinateManipulationsKt.getGeocentricCoords(ra, dec), label, color);
  }

  public TextPrimitive(float ra, float dec, String label, int color, int outlineColor, float outlineWidth) {
    this(CoordinateManipulationsKt.getGeocentricCoords(ra, dec), label, color, 0.02f, 15, outlineColor, outlineWidth);
  }

  public TextPrimitive(float ra, float dec, String label, int color, float offset, int outlineColor, float outlineWidth) {
    this(CoordinateManipulationsKt.getGeocentricCoords(ra, dec), label, color, offset, 15, outlineColor, outlineWidth);
  }

  public TextPrimitive(Vector3 coords, String label, int color) {
    this(coords, label, color, 0.02f, 15);
  }

  public TextPrimitive(Vector3 coords, String label, int color, float offset,
                       int fontSize) {
    this(coords, label, color, offset, fontSize, 0, 0f);
  }

  public TextPrimitive(Vector3 coords, String label, int color, float offset,
                       int fontSize, int outlineColor, float outlineWidth) {

    super(coords, color);
    this.label = Preconditions.checkNotNull(label);
    Preconditions.checkArgument(!label.trim().isEmpty());

    this.offset = offset;
    this.fontSize = fontSize;
    this.outlineColor = outlineColor;
    this.outlineWidth = outlineWidth;
  }

  public String getText() {
    return label;
  }

  public int getFontSize() {
    return fontSize;
  }

  public float getOffset() {
    return offset;
  }

  public void setText(String newText) {
    label = newText;
  }
}
