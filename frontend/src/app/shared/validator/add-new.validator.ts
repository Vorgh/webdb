import {isColumnTypeValid} from "./shared/utils";

const CASCADE_OPTIONS = ["RESTRICT", "CASCADE", "SET NULL", "NO ACTION"];

export function newColumnValidator(name, type): string
{
    if (name.value == "") return "Name is required.";
    if (type.value == "") return "A column type is required";
    if (!isColumnTypeValid(type.value)) return "Wrong column type format.";

    return null;
}

export function newForeignKeyValidator(name, column, reference, updateRule, deleteRule): string
{
    if (name.value == "") return "Name is required.";
    if (column.value == "") return "A column is required.";
    if (reference.value == "") return "A column reference is required";
    if (updateRule.value == "") return "An update rule is required.";
    if (deleteRule.value == "") return "A delete rule is required.";

    let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]$');
    if (!regex.test(reference.value))
    {
        return "Invalid column reference format."
    }

    if (!CASCADE_OPTIONS.includes(updateRule.value.toUpperCase()))
    {
        return "Invalid update rule"
    }

    if (!CASCADE_OPTIONS.includes(deleteRule.value.toUpperCase()))
    {
        return "Invalid delete rule"
    }

    return null;
}

export function newIndexValidator(name, reference): string
{
    if (name.value == "") return "Name is required.";
    if (reference.value == "") return "A column reference is required";

    let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]$');
    if (!regex.test(reference.value))
    {
        return "Invalid column reference format."
    }

    return null;
}
